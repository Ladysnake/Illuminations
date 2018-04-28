package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemStack implements net.minecraftforge.common.capabilities.ICapabilitySerializable<NBTTagCompound>
{
    public static final ItemStack EMPTY = new ItemStack((Item)null);
    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    /** Size of the stack. */
    private int stackSize;
    /** Number of animation frames to go when receiving an item (by walking into it, for example). */
    private int animationsToGo;
    private final Item item;
    /** An NBTTagCompound containing data about an ItemStack. */
    private NBTTagCompound stackTagCompound;
    private boolean isEmpty;
    int itemDamage;
    /** Item frame this stack is on, or null if not on an item frame. */
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;

    private net.minecraftforge.registries.IRegistryDelegate<Item> delegate;
    private net.minecraftforge.common.capabilities.CapabilityDispatcher capabilities;
    private NBTTagCompound capNBT;

    public ItemStack(Block blockIn)
    {
        this(blockIn, 1);
    }

    public ItemStack(Block blockIn, int amount)
    {
        this(blockIn, amount, 0);
    }

    public ItemStack(Block blockIn, int amount, int meta)
    {
        this(Item.getItemFromBlock(blockIn), amount, meta);
    }

    public ItemStack(Item itemIn)
    {
        this(itemIn, 1);
    }

    public ItemStack(Item itemIn, int amount)
    {
        this(itemIn, amount, 0);
    }

    public ItemStack(Item itemIn, int amount, int meta){ this(itemIn, amount, meta, null); }
    public ItemStack(Item itemIn, int amount, int meta, @Nullable NBTTagCompound capNBT)
    {
        this.capNBT = capNBT;
        this.item = itemIn;
        this.itemDamage = meta;
        this.stackSize = amount;

        if (this.itemDamage < 0)
        {
            this.itemDamage = 0;
        }

        this.updateEmptyState();
        this.forgeInit();
    }

    private void updateEmptyState()
    {
        this.isEmpty = this.isEmpty();
    }

    public ItemStack(NBTTagCompound compound)
    {
        this.capNBT = compound.hasKey("ForgeCaps") ? compound.getCompoundTag("ForgeCaps") : null;
        this.item = compound.hasKey("id", 8) ? Item.getByNameOrId(compound.getString("id")) : Items.AIR; //Forge fix tons of NumberFormatExceptions that are caused by deserializing EMPTY ItemStacks.
        this.stackSize = compound.getByte("Count");
        this.itemDamage = Math.max(0, compound.getShort("Damage"));

        if (compound.hasKey("tag", 10))
        {
            this.stackTagCompound = compound.getCompoundTag("tag");

            if (this.item != null)
            {
                this.item.updateItemStackNBT(compound);
            }
        }

        this.updateEmptyState();
        this.forgeInit();
    }

    public boolean isEmpty()
    {
        if (this == EMPTY)
        {
            return true;
        }
        else if (this.getItemRaw() != null && this.getItemRaw() != Items.AIR)
        {
            if (this.stackSize <= 0)
            {
                return true;
            }
            else
            {
                return this.itemDamage < -32768 || this.itemDamage > 65535;
            }
        }
        else
        {
            return true;
        }
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.ITEM_INSTANCE, new BlockEntityTag());
        fixer.registerWalker(FixTypes.ITEM_INSTANCE, new EntityTag());
    }

    /**
     * Splits off a stack of the given amount of this stack and reduces this stack by the amount.
     */
    public ItemStack splitStack(int amount)
    {
        int i = Math.min(amount, this.stackSize);
        ItemStack itemstack = this.copy();
        itemstack.setCount(i);
        this.shrink(i);
        return itemstack;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Item getItem()
    {
        return this.isEmpty || this.delegate == null ? Items.AIR : this.delegate.get();
    }

    /**
     * Called when the player uses this ItemStack on a Block (right-click). Places blocks, etc. (Legacy name:
     * tryPlaceItemIntoWorld)
     */
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote) return net.minecraftforge.common.ForgeHooks.onPlaceItemIntoWorld(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ, hand);
        EnumActionResult enumactionresult = this.getItem().onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);

        if (enumactionresult == EnumActionResult.SUCCESS)
        {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }

        return enumactionresult;
    }

    public EnumActionResult onItemUseFirst(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        // copy of onitemuse but for onitemusefirst
        EnumActionResult enumactionresult = this.getItem().onItemUseFirst(playerIn, worldIn, pos, side, hitX, hitY, hitZ, hand);

        if (enumactionresult == EnumActionResult.SUCCESS)
        {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }

        return enumactionresult;
    }

    public float getDestroySpeed(IBlockState blockIn)
    {
        return this.getItem().getDestroySpeed(this, blockIn);
    }

    /**
     * Called whenr the item stack is equipped and right clicked. Replaces the item stack with the return value.
     */
    public ActionResult<ItemStack> useItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        return this.getItem().onItemRightClick(worldIn, playerIn, hand);
    }

    /**
     * Called when the item in use count reach 0, e.g. item food eaten. Return the new ItemStack. Args : world, entity
     */
    public ItemStack onItemUseFinish(World worldIn, EntityLivingBase entityLiving)
    {
        return this.getItem().onItemUseFinish(this, worldIn, entityLiving);
    }

    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(this.item);
        nbt.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
        nbt.setByte("Count", (byte)this.stackSize);
        nbt.setShort("Damage", (short)this.itemDamage);

        if (this.stackTagCompound != null)
        {
            nbt.setTag("tag", this.stackTagCompound);
        }

        if (this.capabilities != null)
        {
            NBTTagCompound cnbt = this.capabilities.serializeNBT();
            if (!cnbt.hasNoTags()) nbt.setTag("ForgeCaps", cnbt);
        }

        return nbt;
    }

    /**
     * Returns maximum size of the stack.
     */
    public int getMaxStackSize()
    {
        return this.getItem().getItemStackLimit(this);
    }

    /**
     * Returns true if the ItemStack can hold 2 or more units of the item.
     */
    public boolean isStackable()
    {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    /**
     * true if this itemStack is damageable
     */
    public boolean isItemStackDamageable()
    {
        if (this.isEmpty)
        {
            return false;
        }
        else if (this.item.getMaxDamage(this) <= 0)
        {
            return false;
        }
        else
        {
            return !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable");
        }
    }

    public boolean getHasSubtypes()
    {
        return this.getItem().getHasSubtypes();
    }

    /**
     * returns true when a damageable item is damaged
     */
    public boolean isItemDamaged()
    {
        return this.isItemStackDamageable() && getItem().isDamaged(this);
    }

    public int getItemDamage()
    {
        return getItem().getDamage(this);
    }

    public int getMetadata()
    {
        return getItem().getMetadata(this);
    }

    public void setItemDamage(int meta)
    {
        getItem().setDamage(this, meta);
    }

    /**
     * Returns the max damage an item in the stack can take.
     */
    public int getMaxDamage()
    {
        return this.getItem().getMaxDamage(this);
    }

    /**
     * Attempts to damage the ItemStack with par1 amount of damage, If the ItemStack has the Unbreaking enchantment
     * there is a chance for each point of damage to be negated. Returns true if it takes more damage than
     * getMaxDamage(). Returns false otherwise or if the ItemStack can't be damaged or if all points of damage are
     * negated.
     */
    public boolean attemptDamageItem(int amount, Random rand, @Nullable EntityPlayerMP damager)
    {
        if (!this.isItemStackDamageable())
        {
            return false;
        }
        else
        {
            if (amount > 0)
            {
                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
                int j = 0;

                for (int k = 0; i > 0 && k < amount; ++k)
                {
                    if (EnchantmentDurability.negateDamage(this, i, rand))
                    {
                        ++j;
                    }
                }

                amount -= j;

                if (amount <= 0)
                {
                    return false;
                }
            }

            if (damager != null && amount != 0)
            {
                CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, this, this.itemDamage + amount);
            }

            setItemDamage(getItemDamage() + amount); //Redirect through Item's callback if applicable.
            return getItemDamage() > getMaxDamage();
        }
    }

    /**
     * Damages the item in the ItemStack
     */
    public void damageItem(int amount, EntityLivingBase entityIn)
    {
        if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode)
        {
            if (this.isItemStackDamageable())
            {
                if (this.attemptDamageItem(amount, entityIn.getRNG(), entityIn instanceof EntityPlayerMP ? (EntityPlayerMP)entityIn : null))
                {
                    entityIn.renderBrokenItemStack(this);
                    this.shrink(1);

                    if (entityIn instanceof EntityPlayer)
                    {
                        EntityPlayer entityplayer = (EntityPlayer)entityIn;
                        entityplayer.addStat(StatList.getObjectBreakStats(this.item));
                    }

                    this.itemDamage = 0;
                }
            }
        }
    }

    /**
     * Calls the delegated method to the Item to damage the incoming Entity, and if necessary, triggers a stats
     * increase.
     */
    public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn)
    {
        boolean flag = this.item.hitEntity(this, entityIn, playerIn);

        if (flag)
        {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
    }

    /**
     * Called when a Block is destroyed using this ItemStack
     */
    public void onBlockDestroyed(World worldIn, IBlockState blockIn, BlockPos pos, EntityPlayer playerIn)
    {
        boolean flag = this.getItem().onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);

        if (flag)
        {
            playerIn.addStat(StatList.getObjectUseStats(this.item));
        }
    }

    /**
     * Check whether the given Block can be harvested using this ItemStack.
     */
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return this.getItem().canHarvestBlock(blockIn, this);
    }

    public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn, EnumHand hand)
    {
        return this.getItem().itemInteractionForEntity(this, playerIn, entityIn, hand);
    }

    /**
     * Returns a new stack with the same properties.
     */
    public ItemStack copy()
    {
        ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage, this.capabilities != null ? this.capabilities.serializeNBT() : null);
        itemstack.setAnimationsToGo(this.getAnimationsToGo());

        if (this.stackTagCompound != null)
        {
            itemstack.stackTagCompound = this.stackTagCompound.copy();
        }

        return itemstack;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB)
    {
        if (stackA.isEmpty() && stackB.isEmpty())
        {
            return true;
        }
        else if (!stackA.isEmpty() && !stackB.isEmpty())
        {
            if (stackA.stackTagCompound == null && stackB.stackTagCompound != null)
            {
                return false;
            }
            else
            {
                return (stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)) && stackA.areCapsCompatible(stackB);
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * compares ItemStack argument1 with ItemStack argument2; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
    {
        if (stackA.isEmpty() && stackB.isEmpty())
        {
            return true;
        }
        else
        {
            return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemStackEqual(stackB) : false;
        }
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    private boolean isItemStackEqual(ItemStack other)
    {
        if (this.stackSize != other.stackSize)
        {
            return false;
        }
        else if (this.getItem() != other.getItem())
        {
            return false;
        }
        else if (this.itemDamage != other.itemDamage)
        {
            return false;
        }
        else if (this.stackTagCompound == null && other.stackTagCompound != null)
        {
            return false;
        }
        else
        {
            return (this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound)) && this.areCapsCompatible(other);
        }
    }

    /**
     * Compares Item and damage value of the two stacks
     */
    public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB)
    {
        if (stackA == stackB)
        {
            return true;
        }
        else
        {
            return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemEqual(stackB) : false;
        }
    }

    public static boolean areItemsEqualIgnoreDurability(ItemStack stackA, ItemStack stackB)
    {
        if (stackA == stackB)
        {
            return true;
        }
        else
        {
            return !stackA.isEmpty() && !stackB.isEmpty() ? stackA.isItemEqualIgnoreDurability(stackB) : false;
        }
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if the Items contained in both ItemStacks are
     * equal
     */
    public boolean isItemEqual(ItemStack other)
    {
        return !other.isEmpty() && this.item == other.item && this.itemDamage == other.itemDamage;
    }

    public boolean isItemEqualIgnoreDurability(ItemStack stack)
    {
        if (!this.isItemStackDamageable())
        {
            return this.isItemEqual(stack);
        }
        else
        {
            return !stack.isEmpty() && this.item == stack.item;
        }
    }

    public String getUnlocalizedName()
    {
        return this.getItem().getUnlocalizedName(this);
    }

    public String toString()
    {
        return this.stackSize + "x" + this.getItem().getUnlocalizedName() + "@" + this.itemDamage;
    }

    /**
     * Called each tick as long the ItemStack in on player inventory. Used to progress the pickup animation and update
     * maps.
     */
    public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem)
    {
        if (this.animationsToGo > 0)
        {
            --this.animationsToGo;
        }

        if (this.item != null)
        {
            this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
        }
    }

    public void onCrafting(World worldIn, EntityPlayer playerIn, int amount)
    {
        playerIn.addStat(StatList.getCraftStats(this.item), amount);
        this.getItem().onCreated(this, worldIn, playerIn);
    }

    public int getMaxItemUseDuration()
    {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction()
    {
        return this.getItem().getItemUseAction(this);
    }

    /**
     * Called when the player releases the use item button.
     */
    public void onPlayerStoppedUsing(World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        this.getItem().onPlayerStoppedUsing(this, worldIn, entityLiving, timeLeft);
    }

    /**
     * Returns true if the ItemStack has an NBTTagCompound. Currently used to store enchantments.
     */
    public boolean hasTagCompound()
    {
        return !this.isEmpty && this.stackTagCompound != null;
    }

    /**
     * Returns the NBTTagCompound of the ItemStack.
     */
    @Nullable
    public NBTTagCompound getTagCompound()
    {
        return this.stackTagCompound;
    }

    public NBTTagCompound getOrCreateSubCompound(String key)
    {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10))
        {
            return this.stackTagCompound.getCompoundTag(key);
        }
        else
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            this.setTagInfo(key, nbttagcompound);
            return nbttagcompound;
        }
    }

    /**
     * Get an NBTTagCompound from this stack's NBT data.
     */
    @Nullable
    public NBTTagCompound getSubCompound(String key)
    {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10) ? this.stackTagCompound.getCompoundTag(key) : null;
    }

    public void removeSubCompound(String key)
    {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10))
        {
            this.stackTagCompound.removeTag(key);
        }
    }

    public NBTTagList getEnchantmentTagList()
    {
        return this.stackTagCompound != null ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList();
    }

    /**
     * Assigns a NBTTagCompound to the ItemStack, minecraft validates that only non-stackable items can have it.
     */
    public void setTagCompound(@Nullable NBTTagCompound nbt)
    {
        this.stackTagCompound = nbt;
    }

    /**
     * returns the display name of the itemstack
     */
    public String getDisplayName()
    {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");

        if (nbttagcompound != null)
        {
            if (nbttagcompound.hasKey("Name", 8))
            {
                return nbttagcompound.getString("Name");
            }

            if (nbttagcompound.hasKey("LocName", 8))
            {
                return I18n.translateToLocal(nbttagcompound.getString("LocName"));
            }
        }

        return this.getItem().getItemStackDisplayName(this);
    }

    public ItemStack setTranslatableName(String p_190924_1_)
    {
        this.getOrCreateSubCompound("display").setString("LocName", p_190924_1_);
        return this;
    }

    public ItemStack setStackDisplayName(String displayName)
    {
        this.getOrCreateSubCompound("display").setString("Name", displayName);
        return this;
    }

    /**
     * Clear any custom name set for this ItemStack
     */
    public void clearCustomName()
    {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");

        if (nbttagcompound != null)
        {
            nbttagcompound.removeTag("Name");

            if (nbttagcompound.hasNoTags())
            {
                this.removeSubCompound("display");
            }
        }

        if (this.stackTagCompound != null && this.stackTagCompound.hasNoTags())
        {
            this.stackTagCompound = null;
        }
    }

    /**
     * Returns true if the itemstack has a display name
     */
    public boolean hasDisplayName()
    {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");
        return nbttagcompound != null && nbttagcompound.hasKey("Name", 8);
    }

    /**
     * Return a list of strings containing information about the item
     */
    @SideOnly(Side.CLIENT)
    public List<String> getTooltip(@Nullable EntityPlayer playerIn, ITooltipFlag advanced)
    {
        List<String> list = Lists.<String>newArrayList();
        String s = this.getDisplayName();

        if (this.hasDisplayName())
        {
            s = TextFormatting.ITALIC + s;
        }

        s = s + TextFormatting.RESET;

        if (advanced.isAdvanced())
        {
            String s1 = "";

            if (!s.isEmpty())
            {
                s = s + " (";
                s1 = ")";
            }

            int i = Item.getIdFromItem(this.item);

            if (this.getHasSubtypes())
            {
                s = s + String.format("#%04d/%d%s", i, this.itemDamage, s1);
            }
            else
            {
                s = s + String.format("#%04d%s", i, s1);
            }
        }
        else if (!this.hasDisplayName() && this.item == Items.FILLED_MAP)
        {
            s = s + " #" + this.itemDamage;
        }

        list.add(s);
        int i1 = 0;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99))
        {
            i1 = this.stackTagCompound.getInteger("HideFlags");
        }

        if ((i1 & 32) == 0)
        {
            this.getItem().addInformation(this, playerIn == null ? null : playerIn.world, list, advanced);
        }

        if (this.hasTagCompound())
        {
            if ((i1 & 1) == 0)
            {
                NBTTagList nbttaglist = this.getEnchantmentTagList();

                for (int j = 0; j < nbttaglist.tagCount(); ++j)
                {
                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
                    int k = nbttagcompound.getShort("id");
                    int l = nbttagcompound.getShort("lvl");
                    Enchantment enchantment = Enchantment.getEnchantmentByID(k);

                    if (enchantment != null)
                    {
                        list.add(enchantment.getTranslatedName(l));
                    }
                }
            }

            if (this.stackTagCompound.hasKey("display", 10))
            {
                NBTTagCompound nbttagcompound1 = this.stackTagCompound.getCompoundTag("display");

                if (nbttagcompound1.hasKey("color", 3))
                {
                    if (advanced.isAdvanced())
                    {
                        list.add(I18n.translateToLocalFormatted("item.color", String.format("#%06X", nbttagcompound1.getInteger("color"))));
                    }
                    else
                    {
                        list.add(TextFormatting.ITALIC + I18n.translateToLocal("item.dyed"));
                    }
                }

                if (nbttagcompound1.getTagId("Lore") == 9)
                {
                    NBTTagList nbttaglist3 = nbttagcompound1.getTagList("Lore", 8);

                    if (!nbttaglist3.hasNoTags())
                    {
                        for (int l1 = 0; l1 < nbttaglist3.tagCount(); ++l1)
                        {
                            list.add(TextFormatting.DARK_PURPLE + "" + TextFormatting.ITALIC + nbttaglist3.getStringTagAt(l1));
                        }
                    }
                }
            }
        }

        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            Multimap<String, AttributeModifier> multimap = this.getAttributeModifiers(entityequipmentslot);

            if (!multimap.isEmpty() && (i1 & 2) == 0)
            {
                list.add("");
                list.add(I18n.translateToLocal("item.modifiers." + entityequipmentslot.getName()));

                for (Entry<String, AttributeModifier> entry : multimap.entries())
                {
                    AttributeModifier attributemodifier = entry.getValue();
                    double d0 = attributemodifier.getAmount();
                    boolean flag = false;

                    if (playerIn != null)
                    {
                        if (attributemodifier.getID() == Item.ATTACK_DAMAGE_MODIFIER)
                        {
                            d0 = d0 + playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                            d0 = d0 + (double)EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
                            flag = true;
                        }
                        else if (attributemodifier.getID() == Item.ATTACK_SPEED_MODIFIER)
                        {
                            d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
                            flag = true;
                        }
                    }

                    double d1;

                    if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2)
                    {
                        d1 = d0;
                    }
                    else
                    {
                        d1 = d0 * 100.0D;
                    }

                    if (flag)
                    {
                        list.add(" " + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey())));
                    }
                    else if (d0 > 0.0D)
                    {
                        list.add(TextFormatting.BLUE + " " + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey())));
                    }
                    else if (d0 < 0.0D)
                    {
                        d1 = d1 * -1.0D;
                        list.add(TextFormatting.RED + " " + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey())));
                    }
                }
            }
        }

        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (i1 & 4) == 0)
        {
            list.add(TextFormatting.BLUE + I18n.translateToLocal("item.unbreakable"));
        }

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (i1 & 8) == 0)
        {
            NBTTagList nbttaglist1 = this.stackTagCompound.getTagList("CanDestroy", 8);

            if (!nbttaglist1.hasNoTags())
            {
                list.add("");
                list.add(TextFormatting.GRAY + I18n.translateToLocal("item.canBreak"));

                for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
                {
                    Block block = Block.getBlockFromName(nbttaglist1.getStringTagAt(j1));

                    if (block != null)
                    {
                        list.add(TextFormatting.DARK_GRAY + block.getLocalizedName());
                    }
                    else
                    {
                        list.add(TextFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (i1 & 16) == 0)
        {
            NBTTagList nbttaglist2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);

            if (!nbttaglist2.hasNoTags())
            {
                list.add("");
                list.add(TextFormatting.GRAY + I18n.translateToLocal("item.canPlace"));

                for (int k1 = 0; k1 < nbttaglist2.tagCount(); ++k1)
                {
                    Block block1 = Block.getBlockFromName(nbttaglist2.getStringTagAt(k1));

                    if (block1 != null)
                    {
                        list.add(TextFormatting.DARK_GRAY + block1.getLocalizedName());
                    }
                    else
                    {
                        list.add(TextFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }

        if (advanced.isAdvanced())
        {
            if (this.isItemDamaged())
            {
                list.add(I18n.translateToLocalFormatted("item.durability", this.getMaxDamage() - this.getItemDamage(), this.getMaxDamage()));
            }

            list.add(TextFormatting.DARK_GRAY + ((ResourceLocation)Item.REGISTRY.getNameForObject(this.item)).toString());

            if (this.hasTagCompound())
            {
                list.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("item.nbt_tags", this.getTagCompound().getKeySet().size()));
            }
        }

        net.minecraftforge.event.ForgeEventFactory.onItemTooltip(this, playerIn, list, advanced);
        return list;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect()
    {
        return this.getItem().hasEffect(this);
    }

    public EnumRarity getRarity()
    {
        return this.getItem().getRarity(this);
    }

    /**
     * True if it is a tool and has no enchantments to begin with
     */
    public boolean isItemEnchantable()
    {
        if (!this.getItem().isEnchantable(this))
        {
            return false;
        }
        else
        {
            return !this.isItemEnchanted();
        }
    }

    /**
     * Adds an enchantment with a desired level on the ItemStack.
     */
    public void addEnchantment(Enchantment ench, int level)
    {
        if (this.stackTagCompound == null)
        {
            this.setTagCompound(new NBTTagCompound());
        }

        if (!this.stackTagCompound.hasKey("ench", 9))
        {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short)Enchantment.getEnchantmentID(ench));
        nbttagcompound.setShort("lvl", (short)((byte)level));
        nbttaglist.appendTag(nbttagcompound);
    }

    /**
     * True if the item has enchantment data
     */
    public boolean isItemEnchanted()
    {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9))
        {
            return !this.stackTagCompound.getTagList("ench", 10).hasNoTags();
        }
        else
        {
            return false;
        }
    }

    public void setTagInfo(String key, NBTBase value)
    {
        if (this.stackTagCompound == null)
        {
            this.setTagCompound(new NBTTagCompound());
        }

        this.stackTagCompound.setTag(key, value);
    }

    /**
     * Returns whether this stack is always allowed to edit the world. Forces {@link
     * net.minecraft.entity.player.EntityPlayer#canPlayerEdit EntityPlayer#canPlayerEdit} to return {@code true}.
     * 
     * @return whether this stack ignores other restrictions on how a player can modify the world.
     * @see Item#canItemEditBlocks
     */
    public boolean canEditBlocks()
    {
        return this.getItem().canItemEditBlocks();
    }

    /**
     * Return whether this stack is on an item frame.
     */
    public boolean isOnItemFrame()
    {
        return this.itemFrame != null;
    }

    /**
     * Set the item frame this stack is on.
     */
    public void setItemFrame(EntityItemFrame frame)
    {
        this.itemFrame = frame;
    }

    /**
     * Return the item frame this stack is on. Returns null if not on an item frame.
     */
    @Nullable
    public EntityItemFrame getItemFrame()
    {
        return this.isEmpty ? null : this.itemFrame;
    }

    /**
     * Get this stack's repair cost, or 0 if no repair cost is defined.
     */
    public int getRepairCost()
    {
        return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }

    /**
     * Set this stack's repair cost.
     */
    public void setRepairCost(int cost)
    {
        if (!this.hasTagCompound())
        {
            this.stackTagCompound = new NBTTagCompound();
        }

        this.stackTagCompound.setInteger("RepairCost", cost);
    }

    /**
     * Gets the attribute modifiers for this ItemStack.
     * Will check for an NBT tag list containing modifiers for the stack.
     */
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9))
        {
            multimap = HashMultimap.<String, AttributeModifier>create();
            NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);

                if (attributemodifier != null && (!nbttagcompound.hasKey("Slot", 8) || nbttagcompound.getString("Slot").equals(equipmentSlot.getName())) && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L)
                {
                    multimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
                }
            }
        }
        else
        {
            multimap = this.getItem().getAttributeModifiers(equipmentSlot, this);
        }

        return multimap;
    }

    public void addAttributeModifier(String attributeName, AttributeModifier modifier, @Nullable EntityEquipmentSlot equipmentSlot)
    {
        if (this.stackTagCompound == null)
        {
            this.stackTagCompound = new NBTTagCompound();
        }

        if (!this.stackTagCompound.hasKey("AttributeModifiers", 9))
        {
            this.stackTagCompound.setTag("AttributeModifiers", new NBTTagList());
        }

        NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
        NBTTagCompound nbttagcompound = SharedMonsterAttributes.writeAttributeModifierToNBT(modifier);
        nbttagcompound.setString("AttributeName", attributeName);

        if (equipmentSlot != null)
        {
            nbttagcompound.setString("Slot", equipmentSlot.getName());
        }

        nbttaglist.appendTag(nbttagcompound);
    }

    /**
     * Get a ChatComponent for this Item's display name that shows this Item on hover
     */
    public ITextComponent getTextComponent()
    {
        TextComponentString textcomponentstring = new TextComponentString(this.getDisplayName());

        if (this.hasDisplayName())
        {
            textcomponentstring.getStyle().setItalic(Boolean.valueOf(true));
        }

        ITextComponent itextcomponent = (new TextComponentString("[")).appendSibling(textcomponentstring).appendText("]");

        if (!this.isEmpty)
        {
            NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
            itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            itextcomponent.getStyle().setColor(this.getRarity().rarityColor);
        }

        return itextcomponent;
    }

    public boolean canDestroy(Block blockIn)
    {
        if (blockIn == this.canDestroyCacheBlock)
        {
            return this.canDestroyCacheResult;
        }
        else
        {
            this.canDestroyCacheBlock = blockIn;

            if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9))
            {
                NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));

                    if (block == blockIn)
                    {
                        this.canDestroyCacheResult = true;
                        return true;
                    }
                }
            }

            this.canDestroyCacheResult = false;
            return false;
        }
    }

    /**
     * Returns whether this stack is explicitly allowed to be used on the given block via the {@code "CanPlaceOn"} NBT
     * tag.
     * 
     * @return whether the {@code "CanPlaceOn"} tag contains the given block.
     *  
     * @param blockIn the block the NBT is being tested for
     */
    public boolean canPlaceOn(Block blockIn)
    {
        if (blockIn == this.canPlaceOnCacheBlock)
        {
            return this.canPlaceOnCacheResult;
        }
        else
        {
            this.canPlaceOnCacheBlock = blockIn;

            if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9))
            {
                NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));

                    if (block == blockIn)
                    {
                        this.canPlaceOnCacheResult = true;
                        return true;
                    }
                }
            }

            this.canPlaceOnCacheResult = false;
            return false;
        }
    }

    public int getAnimationsToGo()
    {
        return this.animationsToGo;
    }

    public void setAnimationsToGo(int animations)
    {
        this.animationsToGo = animations;
    }

    public int getCount()
    {
        return this.isEmpty ? 0 : this.stackSize;
    }

    public void setCount(int size)
    {
        this.stackSize = size;
        this.updateEmptyState();
    }

    public void grow(int quantity)
    {
        this.setCount(this.stackSize + quantity);
    }

    public void shrink(int quantity)
    {
        this.grow(-quantity);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return this.isEmpty  || this.capabilities == null ? false : this.capabilities.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return this.isEmpty  || this.capabilities == null ? null : this.capabilities.getCapability(capability, facing);
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        // TODO do this better while respecting new rules
        final ItemStack itemStack = new ItemStack(nbt);
        this.stackTagCompound = itemStack.stackTagCompound;
        this.capNBT = itemStack.capNBT;
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound ret = new NBTTagCompound();
        this.writeToNBT(ret);
        return ret;
    }

    public boolean areCapsCompatible(ItemStack other)
    {
        if (this.capabilities == null)
        {
            if (other.capabilities == null)
            {
                return true;
            }
            else
            {
                return other.capabilities.areCompatible(null);
            }
        }
        else
        {
            return this.capabilities.areCompatible(other.capabilities);
        }
    }

    /**
     * Set up forge's ItemStack additions.
     */
    private void forgeInit()
    {
        Item item = getItemRaw();
        if (item != null)
        {
            this.delegate = item.delegate;
            net.minecraftforge.common.capabilities.ICapabilityProvider provider = item.initCapabilities(this, this.capNBT);
            this.capabilities = net.minecraftforge.event.ForgeEventFactory.gatherCapabilities(this, provider);
            if (this.capNBT != null && this.capabilities != null) this.capabilities.deserializeNBT(this.capNBT);
        }
    }

    /**
     * Internal call to get the actual item, not the delegate.
     * In all other methods, FML replaces calls to this.item with the item delegate.
     */
    @Nullable
    private Item getItemRaw()
    {
        return this.item;
    }

    /**
     * Modeled after ItemStack.areItemStacksEqual
     * Uses Item.getNBTShareTag for comparison instead of NBT and capabilities.
     * Only used for comparing itemStacks that were transferred from server to client using Item.getNBTShareTag.
     */
    public static boolean areItemStacksEqualUsingNBTShareTag(ItemStack stackA, ItemStack stackB)
    {
        if (stackA.isEmpty())
            return stackB.isEmpty();
        else
            return !stackB.isEmpty() && stackA.isItemStackEqualUsingNBTShareTag(stackB);
    }

    /**
     * Modeled after ItemStack.isItemStackEqual
     * Uses Item.getNBTShareTag for comparison instead of NBT and capabilities.
     * Only used for comparing itemStacks that were transferred from server to client using Item.getNBTShareTag.
     */
    private boolean isItemStackEqualUsingNBTShareTag(ItemStack other)
    {
        return this.stackSize == other.stackSize && this.getItem() == other.getItem() && this.itemDamage == other.itemDamage && areItemStackShareTagsEqual(this, other);
    }

    /**
     * Modeled after ItemStack.areItemStackTagsEqual
     * Uses Item.getNBTShareTag for comparison instead of NBT and capabilities.
     * Only used for comparing itemStacks that were transferred from server to client using Item.getNBTShareTag.
     */
    public static boolean areItemStackShareTagsEqual(ItemStack stackA, ItemStack stackB)
    {
        NBTTagCompound shareTagA = stackA.getItem().getNBTShareTag(stackA);
        NBTTagCompound shareTagB = stackB.getItem().getNBTShareTag(stackB);
        if (shareTagA == null)
            return shareTagB == null;
        else
            return shareTagB != null && shareTagA.equals(shareTagB);
    }

    /**
     *
     * Should this item, when held, allow sneak-clicks to pass through to the underlying block?
     *
     * @param world The world
     * @param pos Block position in world
     * @param player The Player that is wielding the item
     * @return
     */
    public boolean doesSneakBypassUse(net.minecraft.world.IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return this.isEmpty() || this.getItem().doesSneakBypassUse(this, world, pos, player);
    }
}
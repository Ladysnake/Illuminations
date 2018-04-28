package net.minecraft.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import java.util.Arrays;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

public class AdvancementRewards
{
    public static final AdvancementRewards EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY);
    private final int experience;
    private final ResourceLocation[] loot;
    private final ResourceLocation[] recipes;
    private final FunctionObject.CacheableFunction function;

    public AdvancementRewards(int experience, ResourceLocation[] loot, ResourceLocation[] recipes, FunctionObject.CacheableFunction function)
    {
        this.experience = experience;
        this.loot = loot;
        this.recipes = recipes;
        this.function = function;
    }

    public void apply(final EntityPlayerMP player)
    {
        player.addExperience(this.experience);
        LootContext lootcontext = (new LootContext.Builder(player.getServerWorld())).withLootedEntity(player).withPlayer(player).withLuck(player.getLuck()).build(); // Forge: add player & luck to LootContext
        boolean flag = false;

        for (ResourceLocation resourcelocation : this.loot)
        {
            for (ItemStack itemstack : player.world.getLootTableManager().getLootTableFromLocation(resourcelocation).generateLootForPools(player.getRNG(), lootcontext))
            {
                if (player.addItemStackToInventory(itemstack))
                {
                    player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    flag = true;
                }
                else
                {
                    EntityItem entityitem = player.dropItem(itemstack, false);

                    if (entityitem != null)
                    {
                        entityitem.setNoPickupDelay();
                        entityitem.setOwner(player.getName());
                    }
                }
            }
        }

        if (flag)
        {
            player.inventoryContainer.detectAndSendChanges();
        }

        if (this.recipes.length > 0)
        {
            player.unlockRecipes(this.recipes);
        }

        final MinecraftServer minecraftserver = player.mcServer;
        FunctionObject functionobject = this.function.get(minecraftserver.getFunctionManager());

        if (functionobject != null)
        {
            ICommandSender icommandsender = new ICommandSender()
            {
                /**
                 * Get the name of this object. For players this returns their username
                 */
                public String getName()
                {
                    return player.getName();
                }
                /**
                 * Get the formatted ChatComponent that will be used for the sender's username in chat
                 */
                public ITextComponent getDisplayName()
                {
                    return player.getDisplayName();
                }
                /**
                 * Send a chat message to the CommandSender
                 */
                public void sendMessage(ITextComponent component)
                {
                }
                /**
                 * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
                 */
                public boolean canUseCommand(int permLevel, String commandName)
                {
                    return permLevel <= 2;
                }
                /**
                 * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the
                 * world, return the coordinates 0, 0, 0
                 */
                public BlockPos getPosition()
                {
                    return player.getPosition();
                }
                /**
                 * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world,
                 * return 0.0D, 0.0D, 0.0D
                 */
                public Vec3d getPositionVector()
                {
                    return player.getPositionVector();
                }
                /**
                 * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the
                 * world, return the overworld
                 */
                public World getEntityWorld()
                {
                    return player.world;
                }
                /**
                 * Returns the entity associated with the command sender. MAY BE NULL!
                 */
                public Entity getCommandSenderEntity()
                {
                    return player;
                }
                /**
                 * Returns true if the command sender should be sent feedback about executed commands
                 */
                public boolean sendCommandFeedback()
                {
                    return minecraftserver.worlds[0].getGameRules().getBoolean("commandBlockOutput");
                }
                public void setCommandStat(CommandResultStats.Type type, int amount)
                {
                    player.setCommandStat(type, amount);
                }
                /**
                 * Get the Minecraft server instance
                 */
                public MinecraftServer getServer()
                {
                    return player.getServer();
                }
            };
            minecraftserver.getFunctionManager().execute(functionobject, icommandsender);
        }
    }

    public String toString()
    {
        return "AdvancementRewards{experience=" + this.experience + ", loot=" + Arrays.toString((Object[])this.loot) + ", recipes=" + Arrays.toString((Object[])this.recipes) + ", function=" + this.function + '}';
    }

    public static class Deserializer implements JsonDeserializer<AdvancementRewards>
        {
            public AdvancementRewards deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
            {
                JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "rewards");
                int i = JsonUtils.getInt(jsonobject, "experience", 0);
                JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "loot", new JsonArray());
                ResourceLocation[] aresourcelocation = new ResourceLocation[jsonarray.size()];

                for (int j = 0; j < aresourcelocation.length; ++j)
                {
                    aresourcelocation[j] = new ResourceLocation(JsonUtils.getString(jsonarray.get(j), "loot[" + j + "]"));
                }

                JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "recipes", new JsonArray());
                ResourceLocation[] aresourcelocation1 = new ResourceLocation[jsonarray1.size()];

                for (int k = 0; k < aresourcelocation1.length; ++k)
                {
                    aresourcelocation1[k] = new ResourceLocation(JsonUtils.getString(jsonarray1.get(k), "recipes[" + k + "]"));
                    IRecipe irecipe = CraftingManager.getRecipe(aresourcelocation1[k]);

                    if (irecipe == null)
                    {
                        throw new JsonSyntaxException("Unknown recipe '" + aresourcelocation1[k] + "'");
                    }
                }

                FunctionObject.CacheableFunction functionobject$cacheablefunction;

                if (jsonobject.has("function"))
                {
                    functionobject$cacheablefunction = new FunctionObject.CacheableFunction(new ResourceLocation(JsonUtils.getString(jsonobject, "function")));
                }
                else
                {
                    functionobject$cacheablefunction = FunctionObject.CacheableFunction.EMPTY;
                }

                return new AdvancementRewards(i, aresourcelocation, aresourcelocation1, functionobject$cacheablefunction);
            }
        }
}
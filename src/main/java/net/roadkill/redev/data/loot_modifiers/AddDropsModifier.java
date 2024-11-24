package net.roadkill.redev.data.loot_modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.roadkill.redev.data.loot_modifiers.util.LootEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddDropsModifier extends LootModifier
{
    public static MapCodec<AddDropsModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(modifier -> modifier.conditions),
            LootEntry.CODEC.listOf().fieldOf("additions").forGetter(modifier -> modifier.additions),
            BuiltInRegistries.ITEM.byNameCodec().listOf().optionalFieldOf("removals", List.of()).forGetter(modifier -> modifier.removals)
    ).apply(inst, AddDropsModifier::new));

    private final List<LootEntry> additions;
    private final List<Item> removals;

    protected AddDropsModifier(LootItemCondition[] conditionsIn, List<LootEntry> additions, List<Item> removals)
    {
        super(conditionsIn);
        this.additions = additions;
        this.removals = removals;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        int lootLevel = context.hasParameter(LootContextParams.ENCHANTMENT_LEVEL)
                        ? context.getParameter(LootContextParams.ENCHANTMENT_LEVEL)
                        : 1;
        for (LootEntry entry : additions)
        {
            int countRange = entry.count().max() - entry.count().min();
            generatedLoot.add(new ItemStack(entry.item(),
                                            context.getRandom().nextIntBetweenInclusive(entry.count().min(), entry.count().max())
                                                    + context.getRandom().nextIntBetweenInclusive(0, countRange * lootLevel)));
        }
        if (!removals.isEmpty())
        {   generatedLoot.removeIf(stack -> removals.contains(stack.getItem()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec()
    {   return CODEC;
    }
}

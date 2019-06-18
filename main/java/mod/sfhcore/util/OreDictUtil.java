package mod.sfhcore.util;

import mod.sfhcore.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictUtil
{
    public static ItemInfo getOreDictEntry(String oreName)
    {
        if(OreDictionary.getOres(oreName).isEmpty())
            return null;
        for(String modid : Config.oreDictPreferenceOrder){
            if(!Loader.isModLoaded(modid))
                continue;
            NonNullList<ItemStack> possibles = NonNullList.create();
            for(int i = 0; i < OreDictionary.getOres(oreName).size(); i++)
            {
            	if(OreDictionary.getOres(oreName).get(i).getItem().getRegistryName().getResourceDomain().equals(modid))
            		possibles.add(OreDictionary.getOres(oreName).get(i));
            }
            if(!possibles.isEmpty())
                return new ItemInfo(possibles.get(0));
        }
        return new ItemInfo(OreDictionary.getOres(oreName).get(0));
    }
}

package mod.sfhcore.proxy;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public interface IVariantProvider
{
	List<Pair<Integer, String>> getVariants();
}

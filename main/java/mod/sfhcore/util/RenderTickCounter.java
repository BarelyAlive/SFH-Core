package mod.sfhcore.util;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RenderTickCounter {
	private static int renderTicks = 0;

	public static int getRenderTicks() {
		return renderTicks;
	}

	@SubscribeEvent
	public static void onRenderTick(final TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.RenderTickEvent.Phase.START)
			renderTicks++;
	}
}
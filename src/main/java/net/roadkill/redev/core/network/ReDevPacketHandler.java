package net.roadkill.redev.core.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.roadkill.redev.ReDev;
import net.roadkill.redev.core.network.message.EntityPosMessage;
import net.roadkill.redev.core.network.message.PhantomTypeSyncMessage;

public class ReDevPacketHandler
{
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ReDev.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init()
    {
        INSTANCE.registerMessage(0, PhantomTypeSyncMessage.class, PhantomTypeSyncMessage::encode, PhantomTypeSyncMessage::decode, PhantomTypeSyncMessage::handle);
        INSTANCE.registerMessage(1, EntityPosMessage.class, EntityPosMessage::encode, EntityPosMessage::decode, EntityPosMessage::handle);
    }
}

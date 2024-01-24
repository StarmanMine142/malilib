package fi.dy.masa.malilib.network.handler;

import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.network.payload.C2SDataPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;

public class C2SDataHandler
{
    public static void send(C2SDataPayload payload) {
        // Server-bound packet sent from the Client
        if (ClientPlayNetworking.canSend(payload.getId())) {
            ClientPlayNetworking.send(payload);
            MaLiLib.printDebug("C2SDataListener#send(): sending payload id: {}", payload.getId());
        }
    }

    public static void receive(C2SDataPayload payload, ServerPlayNetworking.Context ctx) {
        // Server-bound packet received from the Client
        MaLiLib.printDebug("C2SDataListener#receive(): received C2SData Payload (size in bytes): {}", payload.data().readableBytes());
        MaLiLib.printDebug("C2SDataListener#receive(): id: {}", payload.data().readIdentifier());
        String response = payload.data().readString();
        MaLiLib.printDebug("C2SDataListener#receive(): String: {}", response);
        ctx.player().sendMessage(Text.of("Your message has been received by the server:"));
        ctx.player().sendMessage(Text.of("You sent (DATA) to me: "+response));
    }
}

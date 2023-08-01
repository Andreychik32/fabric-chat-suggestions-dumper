package me.haxellio.chatsuggestionsdumper.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

public class ToastService {
    public static void showToast(String title, String message) {
        MinecraftClient.getInstance().getToastManager().add(SystemToast.create(
                MinecraftClient.getInstance(),
                SystemToast.Type.UNSECURE_SERVER_WARNING,
                Text.literal(title),
                Text.literal(message)
        ));
    }
}

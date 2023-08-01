package me.haxellio.chatsuggestionsdumper.mixins;

import com.mojang.brigadier.suggestion.Suggestion;
import me.haxellio.chatsuggestionsdumper.util.SuggestionsContainer;
import me.haxellio.chatsuggestionsdumper.util.ToastService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    private ButtonWidget copyButton;
    @Inject(method = "init", at = @At("HEAD"))
    void onChatOpened(CallbackInfo ci) {
        System.out.println("Chat opened!");
    }

    @Inject(method = "init", at = @At("TAIL"))
    void onInit(CallbackInfo ci) {
        ChatScreen it = ((ChatScreen)(Object) this);
        copyButton = ButtonWidget
            .builder(Text.literal("Copy Suggestions"), button -> {
                ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
                if (SuggestionsContainer.suggestionList == null || !Objects.equals(server.address, SuggestionsContainer.serverIP)) {
                    ToastService.showToast("No Suggestions Present", "Please, type \"/\" in chat window or do something else for suggestion window to appear.");
                    return;
                }
                String suggestionsString = String.join("\r\n", SuggestionsContainer.suggestionList.stream().map(Suggestion::getText).toList());
                suggestionsString = String.format("Server: %s\r\nIP: %s\r\n\r\nCommands:\r\n%s",
                        server.name,
                        server.address,
                        suggestionsString);
                MinecraftClient.getInstance().keyboard.setClipboard(suggestionsString);
                ToastService.showToast("Success!", "Suggestions copied!");
            })
            .dimensions(it.width - 102, it.height - 36, 100, 20)
            .build();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        copyButton.render(context, mouseX, mouseY, delta);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        copyButton.mouseClicked(mouseX, mouseY, button);
    }
}

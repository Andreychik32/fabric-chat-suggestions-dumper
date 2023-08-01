package me.haxellio.chatsuggestionsdumper.mixins;

import com.mojang.brigadier.suggestion.Suggestions;
import me.haxellio.chatsuggestionsdumper.util.SuggestionsContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChatInputSuggestor.class)
public class ChatSuggestionsMixin {
    @Inject(method = "show", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatInputSuggestor$SuggestionWindow;<init>(Lnet/minecraft/client/gui/screen/ChatInputSuggestor;IIILjava/util/List;Z)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    void getChatSuggestions(boolean narrateFirstSuggestion, CallbackInfo ci, Suggestions suggestions) {
        System.out.println("====================================");
        suggestions.getList().forEach(suggestion -> {
            System.out.println(suggestion.getText());
        });
        System.out.println("====================================");
        SuggestionsContainer.suggestionList = suggestions.getList();
        SuggestionsContainer.serverIP = MinecraftClient.getInstance().getCurrentServerEntry().address;
    }
}

/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org>
 */
package io.github.fablabsmc.fablabs.mixin.networking;

import io.github.fablabsmc.fablabs.impl.networking.client.ClientPlayNetworkAddon;
import io.github.fablabsmc.fablabs.impl.networking.client.ClientPlayNetworkHandlerHook;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerHook {
	
	private ClientPlayNetworkAddon addon;
	
	@Inject(method = "<init>", at = @At("RETURN"))
	public void networking$ctor(CallbackInfo ci) {
		this.addon = new ClientPlayNetworkAddon((ClientPlayNetworkHandler) (Object) this);
	}
	
	@Inject(method = "onGameJoin", at = @At("RETURN"))
	public void networking$onServerPlayReady(GameJoinS2CPacket packet, CallbackInfo ci) {
		this.addon.sendRegistration();
	}

	@Inject(method = "onCustomPayload", at = @At(value = "HEAD"), cancellable = true)
	public void networking$customPayloadReceivedAsync(CustomPayloadS2CPacket packet, CallbackInfo ci) {
		if (this.addon.handle(packet)) {
			ci.cancel();
		}
	}

	@Override 
	public ClientPlayNetworkAddon getAddon() {
		return this.addon;
	}
}

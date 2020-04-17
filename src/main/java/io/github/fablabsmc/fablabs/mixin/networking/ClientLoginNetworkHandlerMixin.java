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

import io.github.fablabsmc.fablabs.impl.networking.client.ClientLoginNetworkAddon;
import io.github.fablabsmc.fablabs.impl.networking.client.ClientLoginNetworkHandlerHook;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public abstract class ClientLoginNetworkHandlerMixin implements ClientLoginNetworkHandlerHook {

	private ClientLoginNetworkAddon addon;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void networking$ctor(CallbackInfo ci) {
		this.addon = new ClientLoginNetworkAddon((ClientLoginNetworkHandler) (Object) this);
	}

	@Inject(method = "onQueryRequest", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", remap = false, shift = At.Shift.AFTER), cancellable = true)
	public void networking$onQueryRequest(LoginQueryRequestS2CPacket packet, CallbackInfo ci) {
		if (this.addon.handlePacket(packet)) {
			ci.cancel();
		}
	}

	@Override
	public ClientLoginNetworkAddon getAddon() {
		return this.addon;
	}
}

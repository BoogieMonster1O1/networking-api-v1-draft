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

package io.github.fablabsmc.fablabs.impl.networking;

import io.github.fablabsmc.fablabs.api.networking.v1.ListenerContext;
import io.github.fablabsmc.fablabs.api.networking.v1.PacketSender;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

// server login
public abstract class AbstractNetworkAddon<C extends ListenerContext> extends ReceivingNetworkAddon<C> implements PacketSender {
	protected final ClientConnection connection;

	protected AbstractNetworkAddon(BasicPacketReceiver<C> receiver, ClientConnection connection) {
		super(receiver);
		this.connection = connection;
	}

	protected abstract Packet<?> makePacket(Identifier channel, PacketByteBuf buf);

	@Override
	public void sendPacket(Identifier channel, PacketByteBuf buf) {
		this.connection.send(makePacket(channel, buf));
	}

	@Override
	public void sendPacket(Identifier channel, PacketByteBuf buf, GenericFutureListener<? extends Future<? super Void>> callback) {
		this.connection.send(makePacket(channel, buf), callback);
	}
}

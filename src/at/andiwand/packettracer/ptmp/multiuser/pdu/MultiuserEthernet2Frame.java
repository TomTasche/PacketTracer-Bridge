package at.andiwand.packettracer.ptmp.multiuser.pdu;

import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class MultiuserEthernet2Frame extends MultiuserProtocolDataUnit {
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("CIpHeader", MultiuserIPv4Packet.class);
		PAYLOAD_ASSOCIATOR.putEntry("CArpPacket", MultiuserARPPacket.class);
//		PAYLOAD_ASSOCIATOR.putEntry("CIpv6Header", MultiuserIPv6Packet.class);
	}
	
	
	
	
	private MultiuserProtocolDataUnit payload;
	private MACAddress source;
	private MACAddress destination;
	private short unknown1;
	private int type;
	
	
	
	public MultiuserProtocolDataUnit getPayload() {
		return payload;
	}
	public MACAddress getSource() {
		return source;
	}
	public MACAddress getDestination() {
		return destination;
	}
	public short getUnknown1() {
		return unknown1;
	}
	public int getType() {
		return type;
	}
	public void getBytes(PTMPDataWriter writer) {
		Class<? extends MultiuserProtocolDataUnit> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
		
		writer.writeMACAddress(source);
		writer.writeMACAddress(destination);
		writer.writeShort(unknown1);
		writer.writeInt(type);
	}
	
	public void setPayload(MultiuserProtocolDataUnit payload) {
		this.payload = payload;
	}
	public void setSource(MACAddress source) {
		this.source = source;
	}
	public void setDestination(MACAddress destination) {
		this.destination = destination;
	}
	public void setUnknown1(short unknown1) {
		this.unknown1 = unknown1;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	public void parse(PTMPDataReader reader) {
		String payloadName = reader.readString();
		payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
		payload.parse(reader);
		
		source = reader.readMACAddress();
		destination = reader.readMACAddress();
		unknown1 = reader.readShort();
		type = reader.readInt();
	}
	
}
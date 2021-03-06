package at.andiwand.packettracer.ptmp.multiuser.packet;

import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserEthernet2Frame;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserPayloadAssociator;
import at.andiwand.packettracer.ptmp.multiuser.pdu.MultiuserProtocolDataUnit;
import at.andiwand.packettracer.ptmp.packet.PTMPEncodedPacket;


public class MultiuserNetworkPacket extends MultiuserPacket {
	
	public static final int TYPE = TYPE_NETWORK_PACKET;
	
	private static final MultiuserPayloadAssociator PAYLOAD_ASSOCIATOR = new MultiuserPayloadAssociator();
	
	
	static {
		PAYLOAD_ASSOCIATOR.putEntry("CEtherentIIHeader", MultiuserEthernet2Frame.class);
	}
	
	
	
	
	private int random;
	private int linkId;
	private MultiuserProtocolDataUnit payload;
	
	
	
	public MultiuserNetworkPacket(int linkId, MultiuserProtocolDataUnit payload) {
		this(0, linkId, payload);
	}
	public MultiuserNetworkPacket(int random, int linkId,
			MultiuserProtocolDataUnit payload) {
		super(TYPE);
		
		this.random = random;
		this.linkId = linkId;
		this.payload = payload;
	}
	public MultiuserNetworkPacket(PTMPDataReader reader) {
		super(reader);
	}
	public MultiuserNetworkPacket(byte[] packet, int encoding) {
		super(packet, encoding);
	}
	public MultiuserNetworkPacket(PTMPEncodedPacket packet) {
		super(packet);
	}
	public MultiuserNetworkPacket(MultiuserNetworkPacket packet) {
		super(packet);
		
		random = packet.random;
		linkId = packet.linkId;
		payload = packet.payload;
	}
	
	
	
	public int getRandom() {
		return random;
	}
	public int getLinkId() {
		return linkId;
	}
	public MultiuserProtocolDataUnit getPayload() {
		return payload;
	}
	public void getValue(PTMPDataWriter writer) {
		writer.writeInt(random);
		writer.writeInt(linkId);
		
		Class<? extends MultiuserProtocolDataUnit> payloadClass = payload.getClass();
		String payloadName = PAYLOAD_ASSOCIATOR.getPayloadName(payloadClass);
		writer.writeString(payloadName);
		payload.getBytes(writer);
	}
	
	public void setRandom(int random) {
		this.random = random;
	}
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	public void setPayload(MultiuserProtocolDataUnit payload) {
		this.payload = payload;
	}
	
	
	public void parseValue(PTMPDataReader reader) {
		random = reader.readInt();
		linkId = reader.readInt();
		
		String payloadName = reader.readString();
		payload = PAYLOAD_ASSOCIATOR.getPayloadInstance(payloadName);
		payload.parse(reader);
	}
	
	
	protected boolean legalType2(int type) {
		return type == TYPE;
	}
	
}
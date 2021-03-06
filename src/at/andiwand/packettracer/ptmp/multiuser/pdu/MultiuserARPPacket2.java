package at.andiwand.packettracer.ptmp.multiuser.pdu;

import java.net.Inet4Address;
import java.net.Inet6Address;

import at.andiwand.library.network.Assignments;
import at.andiwand.library.network.mac.MACAddress;
import at.andiwand.packettracer.ptmp.PTMPDataReader;
import at.andiwand.packettracer.ptmp.PTMPDataWriter;


public class MultiuserARPPacket2 extends MultiuserProtocolDataUnit {
	
	public static void writeHardwareAddress(Object address, short hardwareType,
			PTMPDataWriter writer) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			MACAddress macAddress = (MACAddress) address;
			writer.writeMACAddress(macAddress);
			break;
		
		default:
			throw new IllegalStateException(
					"The hardware type is not supported");
		}
	}
	public static void writeProtocolAddress(Object address, short protocolType,
			PTMPDataWriter writer) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			Inet4Address inet4Address = (Inet4Address) address;
			writer.writeIP4Addres(inet4Address);
			break;
		case Assignments.Ethernet.TYPE_IPV6:
			Inet6Address inet6Address = (Inet6Address) address;
			writer.writeIP6Addres(inet6Address);
			break;
		
		default:
			throw new IllegalStateException(
					"The protocol type is not supported");
		}
	}
	
	public static Object readHardwareAddress(short hardwareType,
			PTMPDataReader reader) {
		switch (hardwareType) {
		case Assignments.ARP.HARDWARE_TYPE_ETHERNET:
			return reader.readMACAddress();
		
		default:
			throw new IllegalStateException(
					"The hardware type is not supported");
		}
	}
	public static Object readProtocolAddress(short protocolType,
			PTMPDataReader reader) {
		switch (protocolType) {
		case Assignments.Ethernet.TYPE_IPV4:
			return reader.readIP4Addres();
		case Assignments.Ethernet.TYPE_IPV6:
			return reader.readIP6Addres();
		
		default:
			throw new IllegalStateException(
					"The protocol type is not supported");
		}
	}
	
	
	
	
	private short hardwareType;
	private short protocolType;
	private short operation;
	private Object senderHardwareAddress;
	private Object targetHardwareAddress;
	private Object senderProtocolAddress;
	private Object targetProtocolAddress;
	
	
	
	public short getHardwareType() {
		return hardwareType;
	}
	public short getProtocolType() {
		return protocolType;
	}
	public short getOperation() {
		return operation;
	}
	public Object getSenderHardwareAddress() {
		return senderHardwareAddress;
	}
	public Object getTargetHardwareAddress() {
		return targetHardwareAddress;
	}
	public Object getSenderProtocolAddress() {
		return senderProtocolAddress;
	}
	public Object getTargetProtocolAddress() {
		return targetProtocolAddress;
	}
	public void getBytes(PTMPDataWriter writer) {
		writer.writeShort(hardwareType);
		writer.writeShort(protocolType);
		writer.writeByte(Assignments.ARP.getHardwareLength(hardwareType));
		writer.writeByte(Assignments.ARP.getProtocolLength(protocolType));
		writer.writeShort(operation);
		writeHardwareAddress(senderHardwareAddress, hardwareType, writer);
		writeHardwareAddress(targetHardwareAddress, hardwareType, writer);
		writeProtocolAddress(senderProtocolAddress, protocolType, writer);
		writeProtocolAddress(targetProtocolAddress, protocolType, writer);
	}
	
	public void setHardwareType(short hardwareType) {
		this.hardwareType = hardwareType;
	}
	public void setProtocolType(short protocolType) {
		this.protocolType = protocolType;
	}
	public void setOperation(short operation) {
		this.operation = operation;
	}
	public void setSenderHardwareAddress(Object senderHardwareAddress) {
		this.senderHardwareAddress = senderHardwareAddress;
	}
	public void setTargetHardwareAddress(Object targetHardwareAddress) {
		this.targetHardwareAddress = targetHardwareAddress;
	}
	public void setSenderProtocolAddress(Object senderProtocolAddress) {
		this.senderProtocolAddress = senderProtocolAddress;
	}
	public void setTargetProtocolAddress(Object targetProtocolAddress) {
		this.targetProtocolAddress = targetProtocolAddress;
	}
	
	
	public void parse(PTMPDataReader reader) {
		hardwareType = reader.readShort();
		protocolType = reader.readShort();
		
		if (reader.readByte() != Assignments.ARP
				.getHardwareLength(hardwareType))
			throw new IllegalStateException("Illegal hardware address length");
		
		if (reader.readByte() != Assignments.ARP
				.getProtocolLength(protocolType))
			throw new IllegalStateException("Illegal protocol address length");
		
		operation = reader.readShort();
		senderHardwareAddress = readHardwareAddress(hardwareType, reader);
		targetHardwareAddress = readHardwareAddress(hardwareType, reader);
		senderProtocolAddress = readProtocolAddress(protocolType, reader);
		targetProtocolAddress = readProtocolAddress(protocolType, reader);
	}
	
}
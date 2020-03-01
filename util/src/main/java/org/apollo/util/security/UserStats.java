package org.apollo.util.security;

import io.netty.buffer.ByteBuf;
import org.apollo.util.BufferUtil;

public class UserStats {

	public static final int STATS_VERSION = 8;

	private int osType;
	private boolean os64Bit;
	private int osVersion;
	private int javaVendor;
	private int javaMajor;
	private int javaMinor;
	private int javaBuild;
	private int availableProcessors;
	private int gpuDriverYear;
	private int gpuDriverMonth;
	private boolean isConsole;
	private int maxMemory;
	private int cpuCount;
	private int ramAmount;
	private int cpuClockspeed;
	private String gpuName;
	private String directxVersion;
	private int cpuBrandId;
	private String cpuVendor;
	private String cpuBrandString;
	private int[] cpuFeatures = new int[3];
	private int cpuModel;

	public boolean populate(ByteBuf buffer) {
		final var version = buffer.readUnsignedByte();
		if (version != STATS_VERSION) {
			return false;
		}

		osType = buffer.readUnsignedByte();
		os64Bit = buffer.readBoolean();
		osVersion = buffer.readUnsignedByte();
		javaVendor = buffer.readUnsignedByte();
		javaMajor = buffer.readUnsignedByte();
		javaMinor = buffer.readUnsignedByte();
		javaBuild = buffer.readUnsignedByte();
		isConsole = buffer.readBoolean();
		maxMemory = buffer.readUnsignedShort();
		availableProcessors = buffer.readUnsignedByte();
		ramAmount = BufferUtil.readUnsignedMedium(buffer);
		cpuClockspeed = buffer.readUnsignedShort();
		gpuName = BufferUtil.readJagexString(buffer);
		directxVersion = BufferUtil.readJagexString(buffer);
		gpuDriverYear = buffer.readUnsignedByte();
		gpuDriverMonth = buffer.readUnsignedShort();
		cpuVendor = BufferUtil.readJagexString(buffer);
		cpuBrandString = BufferUtil.readJagexString(buffer);
		cpuCount = buffer.readUnsignedByte();
		cpuBrandId = buffer.readUnsignedByte();
		for (int index = 0; index < cpuFeatures.length; index++) {
			cpuFeatures[index] = buffer.readInt();
		}
		cpuModel = buffer.readInt();
		BufferUtil.readJagexString(buffer); // This is always empty so don't know. Possibly mobile related?
		return true;
	}

	public int getOsType() {
		return osType;
	}

	public boolean isOs64Bit() {
		return os64Bit;
	}

	public int getOsVersion() {
		return osVersion;
	}

	public int getJavaVendor() {
		return javaVendor;
	}

	public int getJavaMajor() {
		return javaMajor;
	}

	public int getJavaMinor() {
		return javaMinor;
	}

	public int getJavaBuild() {
		return javaBuild;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public int getGpuDriverYear() {
		return gpuDriverYear;
	}

	public int getGpuDriverMonth() {
		return gpuDriverMonth;
	}

	public boolean isConsole() {
		return isConsole;
	}

	public int getMaxMemory() {
		return maxMemory;
	}

	public int getCpuCount() {
		return cpuCount;
	}

	public int getRamAmount() {
		return ramAmount;
	}

	public int getCpuClockspeed() {
		return cpuClockspeed;
	}

	public String getGpuName() {
		return gpuName;
	}

	public String getDirectxVersion() {
		return directxVersion;
	}

	public int getCpuBrandId() {
		return cpuBrandId;
	}

	public String getCpuVendor() {
		return cpuVendor;
	}

	public String getCpuBrandString() {
		return cpuBrandString;
	}

	public int[] getCpuFeatures() {
		return cpuFeatures;
	}

	public int getCpuModel() {
		return cpuModel;
	}
}

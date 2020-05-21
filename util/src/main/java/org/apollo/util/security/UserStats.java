package org.apollo.util.security;

import io.netty.buffer.ByteBuf;
import org.apollo.util.BufferUtil;

public class UserStats {

	/**
	 * Current version of the {@Link UserStats}.
	 */
	public static final int STATS_VERSION = 8;

	/**
	 * The reported version.
	 */
	private final short version;

	/**
	 * The type of operating system.
	 */
	private final int osType;
	/**
	 * If the operating system uses 64bits.
	 */
	private final boolean os64Bit;
	/**
	 * The version of the OS.
	 */
	private final int osVersion;
	/**
	 * The vendor of the JVM.
	 */
	private final int javaVendor;
	/**
	 * The major version of the active JVM.
	 */
	private final int javaMajor;
	/**
	 * The minor version of the active JVM.
	 */
	private final int javaMinor;
	/**
	 * The build of the active JVM.
	 */
	private final int javaBuild;
	/**
	 * Number of processors available.
	 */
	private final int availableProcessors;
	/**
	 * Driver release year.
	 */
	private final int gpuDriverYear;
	/**
	 * Driver release month.
	 */
	private final int gpuDriverMonth;
	/**
	 * If this is running off a console.
	 */
	private final boolean isConsole;
	/**
	 * Maximum heap memory.
	 */
	private final int maxMemory;
	/**
	 * The number of CPUs on the machine.
	 */
	private final int cpuCount;
	/**
	 * The number of RAM available on the machine.
	 */
	private final int ramAmount;
	/**
	 * The current CPU's clock speed.
	 */
	private final int cpuClockspeed;
	/**
	 * The description of the gpu's driver.
	 */
	private final String gpuDriverDescription;
	/**
	 * The name of the GPU.
	 */
	private final String gpuName;
	/**
	 * The directX version installed.
	 */
	private final String directxVersion;
	/**
	 * The CPU's brand identifier.
	 */
	private final int cpuBrandId;
	/**
	 * The CPU"s vendor identifier.
	 */
	private final String cpuVendor;
	/**
	 * The cpu brand's name.
	 */
	private final String cpuBrandString;
	/**
	 * Features on the CPU.
	 */
	private final int[] cpuFeatures = new int[3];
	/**
	 * The model of the CPU.
	 */
	private final int cpuModel;

	/**
	 * Instantiates a new User stats.
	 *
	 * @param buffer the buffer
	 */
	public UserStats(ByteBuf buffer) {
		version = buffer.readUnsignedByte();
		osType = buffer.readUnsignedByte();
		os64Bit = buffer.readBoolean();
		osVersion = buffer.readUnsignedShort();
		javaVendor = buffer.readUnsignedByte();
		javaMajor = buffer.readUnsignedByte();
		javaMinor = buffer.readUnsignedByte();
		javaBuild = buffer.readUnsignedByte();
		isConsole = buffer.readBoolean();
		maxMemory = buffer.readUnsignedShort();
		availableProcessors = buffer.readUnsignedByte();
		ramAmount = BufferUtil.readUnsignedMedium(buffer);
		cpuClockspeed = buffer.readUnsignedShort();
		gpuDriverDescription = BufferUtil.readJagexString(buffer);
		BufferUtil.readJagexString(buffer); // empty
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
		BufferUtil.readJagexString(buffer); // Empty
	}

	/**
	 * Is valid boolean.
	 *
	 * @return the boolean
	 */
	public boolean isValid() {
		return version == STATS_VERSION;
	}

	/**
	 * Gets version.
	 *
	 * @return the version
	 */
	public short getVersion() {
		return version;
	}

	/**
	 * Gets os type.
	 *
	 * @return the os type
	 */
	public int getOsType() {
		return osType;
	}

	/**
	 * Is os 64 bit boolean.
	 *
	 * @return the boolean
	 */
	public boolean isOs64Bit() {
		return os64Bit;
	}

	/**
	 * Gets os version.
	 *
	 * @return the os version
	 */
	public int getOsVersion() {
		return osVersion;
	}

	/**
	 * Gets java vendor.
	 *
	 * @return the java vendor
	 */
	public int getJavaVendor() {
		return javaVendor;
	}

	/**
	 * Gets java major.
	 *
	 * @return the java major
	 */
	public int getJavaMajor() {
		return javaMajor;
	}

	/**
	 * Gets java minor.
	 *
	 * @return the java minor
	 */
	public int getJavaMinor() {
		return javaMinor;
	}

	/**
	 * Gets java build.
	 *
	 * @return the java build
	 */
	public int getJavaBuild() {
		return javaBuild;
	}

	/**
	 * Gets available processors.
	 *
	 * @return the available processors
	 */
	public int getAvailableProcessors() {
		return availableProcessors;
	}

	/**
	 * Gets gpu driver year.
	 *
	 * @return the gpu driver year
	 */
	public int getGpuDriverYear() {
		return gpuDriverYear;
	}

	/**
	 * Gets gpu driver month.
	 *
	 * @return the gpu driver month
	 */
	public int getGpuDriverMonth() {
		return gpuDriverMonth;
	}

	/**
	 * Is console boolean.
	 *
	 * @return the boolean
	 */
	public boolean isConsole() {
		return isConsole;
	}

	/**
	 * Gets max memory.
	 *
	 * @return the max memory
	 */
	public int getMaxMemory() {
		return maxMemory;
	}

	/**
	 * Gets cpu count.
	 *
	 * @return the cpu count
	 */
	public int getCpuCount() {
		return cpuCount;
	}

	/**
	 * Gets ram amount.
	 *
	 * @return the ram amount
	 */
	public int getRamAmount() {
		return ramAmount;
	}

	/**
	 * Gets cpu clockspeed.
	 *
	 * @return the cpu clockspeed
	 */
	public int getCpuClockspeed() {
		return cpuClockspeed;
	}

	/**
	 * Gets gpu driver description.
	 *
	 * @return the gpu driver description
	 */
	public String getGpuDriverDescription() {
		return gpuDriverDescription;
	}

	/**
	 * Gets gpu name.
	 *
	 * @return the gpu name
	 */
	public String getGpuName() {
		return gpuName;
	}

	/**
	 * Gets directx version.
	 *
	 * @return the directx version
	 */
	public String getDirectxVersion() {
		return directxVersion;
	}

	/**
	 * Gets cpu brand id.
	 *
	 * @return the cpu brand id
	 */
	public int getCpuBrandId() {
		return cpuBrandId;
	}

	/**
	 * Gets cpu vendor.
	 *
	 * @return the cpu vendor
	 */
	public String getCpuVendor() {
		return cpuVendor;
	}

	/**
	 * Gets cpu brand string.
	 *
	 * @return the cpu brand string
	 */
	public String getCpuBrandString() {
		return cpuBrandString;
	}

	/**
	 * Get cpu features int [ ].
	 *
	 * @return the int [ ]
	 */
	public int[] getCpuFeatures() {
		return cpuFeatures;
	}

	/**
	 * Gets cpu model.
	 *
	 * @return the cpu model
	 */
	public int getCpuModel() {
		return cpuModel;
	}
}

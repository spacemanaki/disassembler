package com.spacemanaki.disassembler;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class Method {
  public enum AccessFlag implements HasMask {
    PUBLIC(0x0001),
    PRIVATE(0x0002),
    PROTECTED(0x0004),
    STATIC(0x0008),
    FINAL(0x0010),
    SYNCHRONIZED(0x0020),
    BRIDGE(0x0040),
    VARARGS(0x0080),
    NATIVE(0x0100),
    ABSTRACT(0x0400),
    STRICT(0x0800),
    SYNTHETIC(0x1000);

    private final int mask;
    AccessFlag(int mask) {
      this.mask = mask;
    }

    @Override
    public int getMask() {
      return mask;
    }
  }

  public final EnumSet<AccessFlag> accessFlags;
  public final short nameIndex;
  public final short descriptorIndex;
  public final Attribute[] attributes;

  public Method(EnumSet<AccessFlag> accessFlags, short nameIndex, short descriptorIndex, Attribute[] attributes) {
    this.accessFlags = accessFlags;
    this.nameIndex = nameIndex;
    this.descriptorIndex = descriptorIndex;
    this.attributes = attributes;
  }

  public static EnumSet<AccessFlag> readAccessFlags(DataInputStream in) throws IOException {
    Set<AccessFlag> flags = Utility.readEnumSet(AccessFlag.values(), in.readShort());
    return flags.isEmpty() ? EnumSet.noneOf(AccessFlag.class) : EnumSet.copyOf(flags);
  }

  public static Method read(DataInputStream in) throws IOException {
    return new Method(
          readAccessFlags(in)
        , in.readShort()
        , in.readShort()
        , Attribute.readArray(in)
    );
  }
}

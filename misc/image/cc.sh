#!/bin/sh
cd ../../src
java -Xmx256m -jar rtr.jar test image ../misc/image/plat.i686 ../misc/image/image.cmp
bash ../binDsk/r.sh
mv ../binDsk/binImg/*.tar ../binImg/rtr-i686.tar
for a in amd64 arm32 mips64 ppc64 risc64 s390x sparc sh4 m68k hppa arm64 ; do
  java -Xmx256m -jar rtr.jar test image ../misc/image/plat.$a ../misc/image/image.cmp
  bash ../binDsk/r.sh
  mv ../binDsk/binImg/*.tar ../binImg/
  done

#!/bin/sh
sudo apt-get update
sudo apt-get dist-upgrade
sudo apt-get remove apparmor
sudo apt-get autoremove
sudo apt-get clean
cd ~
mkdir a
wget -O a/rtr.zip http://src.mchome.nop.hu/rtr.zip
unzip a/rtr.zip -d a/
mv ~/a/misc/p4bf/*.p4 ~/rare/p4src/
mv ~/a/misc/p4bf/include/*.p4 ~/rare/p4src/include/
mv ~/a/misc/p4bf/*.py ~/rare/bfrt_python/
mv ~/a/misc/p4bf/*.sh ~/
cd ~
rm -rf a/
sudo dd if=/dev/zero of=/zzz bs=1M
sudo rm /zzz

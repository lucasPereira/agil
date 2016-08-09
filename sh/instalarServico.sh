#!/bin/bash

sudo update-rc.d agil disable
sudo rm -r /etc/init.d/agil
sudo cp servicoAgil.sh /etc/init.d/agil
sudo update-rc.d agil defaults

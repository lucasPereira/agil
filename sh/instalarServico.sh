#!/bin/bash

sudo update-rc.d projetos disable
sudo rm -r /etc/init.d/projetos
sudo cp servicoProjetos.sh /etc/init.d/projetos
sudo update-rc.d projetos defaults

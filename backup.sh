#!/bin/bash -v
install -d /home/rasmuserik/data/backup/borg
rsync -avr root@borg.solsort.com:/var/lib/docker/containers/ /home/rasmuserik/data/backup/borg/containers/
rsync -avr root@borg.solsort.com:whale/ /home/rasmuserik/data/backup/borg/whale/
rsync -avr root@borg.solsort.com:/var/lib/docker/volumes/ /home/rasmuserik/data/backup/borg/volumes/
mv /home/rasmuserik/data/backup/borg.prev.tar.gz /home/rasmuserik/data/backup/borg.prevprev.tar.gz 
mv /home/rasmuserik/data/backup/borg.latest.tar.gz /home/rasmuserik/data/backup/borg.prev.tar.gz 
tar cp /home/rasmuserik/data/backup/borg/containers/*/*.log | gzip -9 > /home/rasmuserik/data/backup/borg-logs.`date +%F`.tar.gz
tar cp /home/rasmuserik/data/backup/borg | gzip -9 > /home/rasmuserik/data/backup/borg.latest.tar.gz
cp /home/rasmuserik/data/backup/borg.latest.tar.gz /home/rasmuserik/data/backup/borg.`date +%Y-%m`.tar.gz 

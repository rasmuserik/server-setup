install -d ~/data/backup
cd ~/data/backup
ssh root@borg.solsort.com "tar cvpP /var/lib/docker/volumes/whale_* /var/lib/docker/containers | xz -9 " > whale-latest.tar.xz
rsync -avz root@bork.solsort.com:/var/lib/docker/containers whale-log
ln -f whale-latest.tar.xz whale-`date +%A`.tar.xz
ln -f whale-latest.tar.xz whale-`date +%Gw%V`.tar.xz

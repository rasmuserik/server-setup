install -d ~/backup
cd ~/backup
ssh root@borg.solsort.com "tar cvjpP /var/lib/docker/volumes/whale_*" > whale-latest.tar.bz2
ln -f whale-latest.tar.bz2 whale-`date +%A`.tar.bz2
ln -f whale-latest.tar.bz2 whale-`date +%Gw%V`.tar.bz2

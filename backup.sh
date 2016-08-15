install -d /home/rasmuserik/data/backup/borg
rsync -avr root@borg.solsort.com:/var/lib/docker/containers/ /home/rasmuserik/data/backup/borg/containers/
rsync -avr root@borg.solsort.com:whale/ /home/rasmuserik/data/backup/borg/whale/
rsync -avr root@borg.solsort.com:/var/lib/docker/volumes/ /home/rasmuserik/data/backup/borg/volumes/
mv /home/rasmuserik/data/backup/borg.prev.tar.xz /home/rasmuserik/data/backup/borg.prevprev.tar.xz 
mv /home/rasmuserik/data/backup/borg.latest.tar.xz /home/rasmuserik/data/backup/borg.prev.tar.xz 
tar cvp /home/rasmuserik/data/backup/borg | xz -9 > /home/rasmuserik/data/backup/borg.latest.tar.xz
cp /home/rasmuserik/data/backup/borg.latest.tar.xz /home/rasmuserik/data/backup/borg-all.`date +%Y-%m`.tar.xz 
tar cvp /home/rasmuserik/data/backup/borg/containers/*/*.log | xz -9 > /home/rasmuserik/data/backup/borg-logs.`date +%F`.tar.xz


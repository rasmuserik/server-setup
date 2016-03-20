rm -f dump.tar.bz2
for x in tinkuy-nodebb tinkuy-redis
do
  echo dumping $x
  tar rpPf dump.tar /var/lib/docker/volumes/whale_$x
done
bzip2 -9 dump.tar

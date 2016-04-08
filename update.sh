ssh root@borg.solsort.com "
  source ~/.pw &&
  cd whale &&
  git pull &&
  docker-compose up" || exit 1

if [[ -n $1 ]]
then
  ssh root@borg.solsort.com "source ~/.pw && cd whale && docker kill whale_${1}_1; docker-compose up -d"
fi

ssh root@borg.solsort.com "
  cd apps &&
  git pull &&
  ./update.sh" &

ssh root@borg.solsort.com "cd whale; docker-compose logs"

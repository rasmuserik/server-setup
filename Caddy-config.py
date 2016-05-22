#!/usr/bin/env python

def get(arr, pos, default):
    try:
        return arr[pos]
    except IndexError:
        return default

for line in open('Caddy-orig'):
    print line,
for line in open('projects.lst'):
    parts = line.strip().split(' ')
    kind = parts[1]
    if kind == 'git':
      user = get(parts, 3, 'solsort')
      repos = parts[2]
  
      print repos + '.solsort.com {'
      print '  git github.com/' + user + '/' + repos + ' {'
      print '    hook /git-update'
      print '  }'
      print '  root /apps/' + repos
      print '  cors'
      print '}'

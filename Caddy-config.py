for line in open('Caddy-orig'):
    print line,
for line in open('Caddy-repos'):
    parts = line.strip().split('/')
    user = parts[0]
    repos = parts[1]

    print repos + '.solsort.com {'
    print '  git github.com/' + user + '/' + repos + ' /' + repos + ' {'
    print '    hook /git-update'
    print '  }'
    print '  root /' + repos + '/' + repos
    print '  cors'
    print '}'

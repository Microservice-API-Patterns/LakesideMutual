#/bin/sh

npx recursive-install

npx concurrently \
  --kill-others \
  --names "\
customer-core,\
customer-management-backend,\
customer-management-frontend"\
  --prefix-colors "\
bgRed.bold \
bgYellow.bold,\
bgBlue.bold" \
  \
  "cd customer-core && mvn spring-boot:run" \
  "cd customer-management-backend && mvn spring-boot:run" \
  "cd customer-management-frontend && npm start"
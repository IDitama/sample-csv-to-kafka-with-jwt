# sample-csv-to-kafka-with-jwt

user name dan password yang dapat digunakan
username   | password
user       | user
admin      | admin


CURL untuk auth > 
curl --location 'localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username":"user",
    "password":"user"
}'


CURL untuk upload csv/excel > 
curl --location 'localhost:8080/resource/upload' \
--header 'Authorization: Bearer token after auth' \
--form 'file=@"spreadsheet.xlsx or file.csv"'

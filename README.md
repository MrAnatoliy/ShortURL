# ShortURL Service
This is a simple backend application for managing short urls with redirects and TTL limits

## Instalation
1. Clone a repository `git clone https://github.com/MrAnatoliy/ShortURL.git`

2. Get into project folder `cd ShortURL`

3. Run maven targets :
- For Windows `mvnw.cmd clean package`
- For Unix `./mvnw clean package`

4. Run jar file `java -jar target/*.jar`

## Usage
**WARNING -** This is a full REST application so there is no CLI commands. Shell can only be used for checking logs for notifications and other useful stuff

For using this application you can use either Postman or curl

### 1. Creating new URL
Curl:
```shell
curl -X POST "http://localhost:8080/createShortUrl" \
  -H "Content-Type: application/json" \
  -d '{"longURL":"https://github.com/spring-projects/spring-boot","userUUID":"9f49a207-6d83-4d15-9543-79a87db1da1a"}'
```
Postman: <br>
**POST** http://localhost:8080/createShortUrl
**Body/raw**
```json
{
    "longURL": "https://habr.com/ru/companies/piter/articles/676394/",
    "userUUID": "9f49a207-6d83-4d15-9543-79a87db1da1a"
}
```

### 2. Redirect by short code
Curl:
`curl -L "http://localhost:8080/open/PASTE_GENERATED_URL_HERE"`
Postman: 

**GET** http://localhost:8080/open/PASTE_GENERATED_URL_HERE
*(Enable “Automatically follow redirects” in Settings)*

### 3. Update TTL & max redirects (owner only)
Curl:
```shell
curl -X PATCH "http://localhost:8080/urls/PASTE_GENERATED_URL_HERE" \
  -H "Content-Type: application/json" \
  -d '{"userUUID":"9f49a207-6d83-4d15-9543-79a87db1da1a","ttlSeconds":7200,"maxRequests":50}'
```

Postman: <br>
**PATCH** http://localhost:8080/urls/PASTE_GENERATED_URL_HERE
**Body/raw**

```json
{
    "userUUID": "9f49a207-6d83-4d15-9543-79a87db1da1a",
    "ttlSeconds": 7200,
    "maxRequests": 50
}
```

### 4. Delete entry (owner only)
Curl:
```shell
curl -X DELETE "http://localhost:8080/urls/PASTE_GENERATED_URL_HERE" \
  -H "Content-Type: application/json" \
  -d '{"userUUID":"9f49a207-6d83-4d15-9543-79a87db1da1a"}'
```
Postman: <br>
**DELETE** http://localhost:8080/urls/PASTE_GENERATED_URL_HERE
**Body/raw**
```json
{
    "userUUID": "9f49a207-6d83-4d15-9543-79a87db1da1a"
}
```

### 5. List all URLs of a user
Curl:
`curl "http://localhost:8080/urls/user/9f49a207-6d83-4d15-9543-79a87db1da1a"`
Postman: 

**GET** http://localhost:8080/urls/user/9f49a207-6d83-4d15-9543-79a87db1da1a
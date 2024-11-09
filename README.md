# roadmap-weather
https://roadmap.sh/projects/weather-api-wrapper-service

# How to build
## Store the API key
Into `roadmap-weather.api.visual-crossing.key`
```json
# application.yaml
roadmap-weather:
  api:
    visual-crossing:
      url: https://weather.visualcrossing.com
      key: insert-your-key
```

## Run Redis
Using [Rancher desktop](https://rancherdesktop.io/) or [Docker desktop](https://www.docker.com/products/docker-desktop/)  
And run docker compose
```sh
D:\github\roadmap-weather> docker compose up -d
[+] Running 1/1
 ✔ Container weather-redis  Started
```
And run project.

# Before and after using cache

## Before
**total 1,805 ms**

![image](https://github.com/user-attachments/assets/e270ed6d-ece8-476d-ba42-c2053817f895)

## After
**total 11ms**

![image](https://github.com/user-attachments/assets/e589b509-54a1-400b-a1a4-0e0d1f93fd60)

# Client error handling
```java
class FeignErrorDecoder : ErrorDecoder {
    private val defaultDecoder: ErrorDecoder = ErrorDecoder.Default()
    override fun decode(p0: String?, p1: Response): Exception {
        val lastEndpoint: String = p1.request().requestTemplate().path().split("/").last()
        if (p1.status() == 400) {
            throw InvalidParameterException("Bad Request Parameter '${lastEndpoint}'")
        } else if (p1.status() in 500..504) {
            throw ClientUnknownException("${p1.status()} Server error '${lastEndpoint}'")
        }
        return defaultDecoder.decode(p0, p1)
    }
}
```

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
```kotlin
class FeignErrorDecoder : ErrorDecoder {
    private val defaultDecoder: ErrorDecoder = ErrorDecoder.Default()
    override fun decode(p0: String?, p1: Response): Exception {
        val lastEndpoint: String = p1.request().requestTemplate().path().split("/").last()
        if (p1.status() == 400) { // if the city code is invalid
            throw InvalidParameterException("Bad Request Parameter '${lastEndpoint}'")
        } else if (p1.status() in 500..504) { // the 3rd party API is down
            throw ClientUnknownException("${p1.status()} Server error '${lastEndpoint}'")
        }
        return defaultDecoder.decode(p0, p1)
    }
}
```
handling `Bad Request` if the city code is invalid  
`if (p1.status() == 400) { // if the city code is invalid`

handling `500~504` if the 3rd party API is down  
`} else if (p1.status() in 500..504) { // the 3rd party API is down`

# HTTP requests
## Using openfeign
```kotlin
implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
```
```kotlin
@FeignClient("weather", url = "\${roadmap-weather.api.visual-crossing.url}", configuration = [FeignClientConfiguration::class])
fun interface WeatherCrossingClient {

    @GetMapping("/VisualCrossingWebServices/rest/services/timeline/{location}")
    @Cacheable(cacheNames = ["weathers"], key = "#location")
    fun getVisualCrossingWebServices(
        @PathVariable location: String,
        @RequestParam unitGroup: String,
        @RequestParam key: String,
        @RequestParam contentType: String
    ): WeatherCrossingApiResponseDto?
}
```

# Rate limiting to prevent abuse of API
## Using resilience4j
### config
```yaml
resilience4j:
  ratelimiter:
    instances:
      weathers:
        limit-refresh-period: 20s
        limit-for-period: 5
        timeout-duration: 2s
```
Allows up to 5 requests in 20 seconds.  
And set maximum time. when request is blocked to 2 seconds

### Controller
```kotlin
@GetMapping("/{location}")
@RateLimiter(name = "weathers")
fun get(
    @PathVariable location: String
): ResponseEntity<WeatherCrossingApiResponseDto> {
    return ResponseEntity.ok()
        .body(weatherCrossingService
            .getVisualCrossingWebJson(location))
}
```
Using `@RateLimiter(name = "weathers")` weathers instance.

### RateLimiter error handling
![image](https://github.com/user-attachments/assets/d0e9ea25-91e8-4f87-bb8c-c56e920f8993)

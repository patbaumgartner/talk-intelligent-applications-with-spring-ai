# Asking for a Weather Forecast in the US

Once the client is up and running, it will be listening on port `8080`.

You can then submit questions by sending a POST request to the `/ask` endpoint with a JSON payload containing a `"question"` property.

## Examples using `curl`

### Forecast for Tomorrow

```bash
curl -H "Content-Type: application/json" \
     -X POST http://localhost:8080/ask \
     -d '{"question":"What is the forecast for Yellowstone National Park, Wyoming, USA, for tomorrow?"}'
```

### Forecast for a Specific City

```bash
curl -H "Content-Type: application/json" \
     -X POST http://localhost:8080/ask \
     -d '{"question":"What is the weather forecast for Miami, Florida, USA, this weekend?"}'
```

### Forecast for a Specific Date

```bash
curl -H "Content-Type: application/json" \
     -X POST http://localhost:8080/ask \
     -d '{"question":"Will it rain in Seattle, Washington, USA, on May 5th?"}'
```

### Forecast for a National Park

```bash
curl -H "Content-Type: application/json" \
     -X POST http://localhost:8080/ask \
     -d '{"question":"How will the weather be at Grand Canyon National Park, Arizona, USA, next Thursday?"}'
```

#What it does

This application receives docker event when a docker container goes online, and updates appropriate dns record, if
configured, accordingly.

#Getting Started

This application requires one's Cloudflare credentials to work properly. Create a file `credential.conf` as follows under 
`src/main/resources` folder and enter appropriate credentials.
```hocon
cloudflare {
  email = // Cloudflare email address
  key = // Cloudflare key
  zone {
    id = // id of the zone that the DNS servers are located at
  }
}
```

#Missing features

1. Interface with docker and receive events.
2. Read DNS configuration from docker container environment.
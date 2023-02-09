# ![Lakeside Mutual Logo](./resources/logo-32x32.png) Kubernetes

## Google Cloud Kubernetes Engine

This is an experimental deployment that has been tested on the Google Cloud Kubernetes Engine. The following steps assume that you have created a GKE Cluster and have all the command line tools set up.

### Building

1. Build the images by running `docker-compose build` (this can take 15-20 minutes on the first run).
1. Push all local images to the Google Container Registry `docker images | grep gcr.io | cut -f1 -d' ' | xargs -L 1 docker push`
1. Create a public IP address `gcloud compute addresses create my-public-ip` and replace all occurrences of `34.65.192.94` in the manifests with your own IP.
1. Create the deployments and services: `kubectl apply -f manifests`

## Tipps & Tricks

Updating a deployment after updating the image:

```kubectl rollout restart deployment $THE_DEPLOYMENT```

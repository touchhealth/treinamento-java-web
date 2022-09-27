#!/bin/bash

docker build \
  --target dependencias \
  --cache-from treinamento-java-web:dependencias \
  -t treinamento-java-web:dependencias \
  .

docker build \
  --target builder \
  --cache-from treinamento-java-web:dependencias \
  -t treinamento-java-web:builder \
  .

docker build \
  --cache-from treinamento-java-web:dependencias \
  --cache-from treinamento-java-web:builder \
  --cache-from treinamento-java-web:latest \
  -t treinamento-java-web:latest \
  .
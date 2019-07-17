FROM mono:latest

RUN apt-get update -y && apt-get install -y unzip

RUN mkdir -p /docfx
WORKDIR /docfx
RUN curl -L https://github.com/dotnet/docfx/releases/download/v2.43.3/docfx.zip -O \
  && unzip docfx.zip \
  && chmod +r /docfx

RUN echo "#!/bin/bash" >> /usr/local/bin/docfx \
   && echo "mono /docfx/docfx.exe \"\$@\"" >> /usr/local/bin/docfx \
   && chmod +x /usr/local/bin/docfx

RUN mkdir -p /srv/project \
  && useradd -u 1000 docfx \
  && chown docfx:docfx /srv/project

USER docfx
WORKDIR /srv/project

ENTRYPOINT ["docfx"]
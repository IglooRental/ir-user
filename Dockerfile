FROM ubuntu:trusty

MAINTAINER jm5619

RUN sudo echo -e "deb http://ppa.launchpad.net/openjdk-r/ppa/ubuntu trusty main \
	\ndeb-src http://ppa.launchpad.net/openjdk-r/ppa/ubuntu trusty main" >> /etc/apt/sources.list \
	&& sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys DA1A4A13543B466853BAF164EB9B1D8886F44E2A \
	&& sudo apt-get update \
	&& sudo apt-get install -y openjdk-8-jdk \
	&& mkdir /app

WORKDIR /app

ADD ./target/ir-user-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "ir-user-1.0.0-SNAPSHOT.jar"]

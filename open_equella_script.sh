#!/bin/bash

# first enter into machine navigate to project location and down the application

echo "kapil1"
echo "$PWD"
cd /home/ubuntu/openEQUELLA
git pull
echo "$PWD"
cd docker
sudo docker-compose down


# prune the images 

sudo docker rm $(sudo docker images -q)


# go to project root folder

cd ..


# the zip file will be created at "openequella/Installer/target/" so delete all the zip files starting with equella-installer*
echo "$PWD"

cd Installer/target/
rm -rf equella-installer*


# clean sbt and run command to build installer
# this command will create the installer in openequella/Installer/target/equella-Installer-2023.1.0.zip (use wildcard for this)
cd ../..
echo "kapil2"
echo "$PWD"
cd /home/ubuntu/openEQUELLA/
./sbt clean
./sbt installerZip
echo "kapil3"


# check for the installer file if present copy it to docker folder
# change the name of the copied zip

echo "before if condition"
cd Installer/target/
File=equella-installer-2022.2.0.zip  
if [ -f "$File" ]; then  
echo "kapil4"
echo "entered if condition"
mv ./equella-installer-2022.2.0.zip ./installer.zip
echo "equella-installer name changed"
cd ../../docker
current_time=$(date "+%Y.%m.%d-%H.%M.%S")
mv ./installer.zip ./installer.zip."$current_time"
cd ..
echo "changed the existing installer.zip name to old-installer.zip in docker folder"
cp ./Installer/target/installer.zip ./docker/
echo "copied installer.zip form target folder to docker"
else
echo "New installer not built, creating the image with the old installer.zip"
fi


#run the application through docker

cd docker
sudo docker-compose up -d


# there will be an image created with name docker_oeq
# tag it and push it to ECR.
echo "kapil5"
echo "Enter the tag name for the docker image"
read tag
echo "kapil6"
sudo aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 598864331813.dkr.ecr.ap-south-1.amazonaws.com
echo "login successful"
sudo docker tag docker_oeq:latest 598864331813.dkr.ecr.ap-south-1.amazonaws.com/custom_content_repo:"$tag"
echo "tagged the image"
echo "kapil7"
docker push 598864331813.dkr.ecr.ap-south-1.amazonaws.com/custom_content_repo:"$tag"
echo "kapil8"


# sudo docker tag docker_oeq:latest kapilbunni/open:"$tag"
# sudo docker push kapilbunni/open:"$tag"

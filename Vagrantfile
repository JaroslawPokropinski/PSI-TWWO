# plik Vagrantfile
Vagrant.configure("2") do |config|
    config.vm.box = "gusztavvargadr/docker-linux"
    config.vm.hostname = "jenkins-build-node"
    config.vm.network "private_network", ip: "192.168.99.160"
    config.vm.provider "virtualbox" do |vb|
    config.disksize.size = '50GB'
    # Display the VirtualBox GUI when booting the machine
    # vb.gui = true
    # Customize the amount of memory on the VM:
    vb.cpus = "2"
    vb.memory = "4096"
    end
    config.vm.provision "shell", inline: <<-SHELL
        apt-get update
        apt-get install git apt-transport-https \
        ca-certificates curl \
        software-properties-common -y

        wget https://download.java.net/java/GA/jdk14.0.2/205943a0976c4ed48cb16f1043c5c647/12/GPL/openjdk-14.0.2_linux-x64_bin.tar.gz
        tar xvf openjdk-14.0.2_linux-x64_bin.tar.gz
        mv jdk-14.0.2 /usr/lib/jvm
        update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk-14.0.2/bin/javac" 3
        update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk-14.0.2/bin/java" 3
        update-alternatives --set "javac" "/usr/lib/jvm/jdk-14.0.2/bin/javac"
        update-alternatives --set "java" "/usr/lib/jvm/jdk-14.0.2/bin/java"
        
        curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
	    sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

	
    SHELL
end

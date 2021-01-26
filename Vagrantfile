# plik Vagrantfile
Vagrant.configure("2") do |config|
    config.vm.box = "gusztavvargadr/docker-linux"
    config.vm.hostname = "jenkins-build-node"
    config.vm.network "private_network", ip: "192.168.99.160"
    config.vm.provider "virtualbox" do |vb|
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

        sudo add-apt-repository ppa:openjdk-r/ppa
        sudo apt-get update
        sudo apt install openjdk-11-jdk -y
    SHELL
end
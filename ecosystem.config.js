module.exports = {
  apps: [
    {
      name: 'destrostudios',
      script: '/usr/lib/jvm/java-17-openjdk-amd64/bin/java',
      args: '-jar destrostudios.jar --spring.profiles.active=prod',
      exp_backoff_restart_delay: 100,
    },
  ],
};

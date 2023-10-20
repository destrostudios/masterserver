module.exports = {
  apps: [
    {
      name: 'cards',
      script: '/usr/lib/jvm/java-17-openjdk-amd64/bin/java',
      args: '-jar cards.jar',
      exp_backoff_restart_delay: 100,
    },
  ],
};

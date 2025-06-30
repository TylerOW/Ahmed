const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const readline = require('readline');
const { Driver } = require('../src/models');
require('dotenv').config();

/**
 * Inserts a driver into MongoDB.
 * If `--interactive` is passed as a CLI argument, prompts for input.
 * Otherwise, inserts a default sample driver.
 */
async function main() {
  if (!process.env.MONGO_URI) {
    throw new Error('MONGO_URI is not defined in environment');
  }

  await mongoose.connect(process.env.MONGO_URI);

  let name, phone, email, password;

  if (process.argv.includes('--interactive')) {
    const rl = readline.createInterface({
      input: process.stdin,
      output: process.stdout,
    });

    const ask = (q) => new Promise((res) => rl.question(q, (ans) => res(ans.trim())));

    name = await ask('Driver name: ');
    phone = await ask('Phone number: ');
    email = await ask('Email: ');
    password = await ask('Password: ');
    rl.close();
  } else {
    console.log('Inserting default demo driver...');
    name = 'Demo Driver';
    phone = '60123456789';
    email = 'driver@example.com';
    password = 'driver123';
  }

  const passwordHash = await bcrypt.hash(password, 10);
  const driver = await Driver.create({ name, phone, email, password: passwordHash, isDriver: true });

  console.log('✅ Inserted driver with ID:', driver._id);
  await mongoose.disconnect();
}

main().catch(err => {
  console.error('❌ Failed to insert driver:', err);
  process.exit(1);
});

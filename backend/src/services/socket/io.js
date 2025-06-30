let ioInstance;
function initIO(io){
  ioInstance = io;
}
function getIO(){
  return ioInstance;
}
module.exports = { initIO, getIO };

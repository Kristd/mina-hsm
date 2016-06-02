import socket

SJL06_IP = '99.12.38.74'
SJL06_Port = 8529

msg = 'KMC MakeMAC   12345678'
msg = chr(len(msg)/256) + chr(len(msg)%256) + msg

if __name__ == '__main__':
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM, 0)
    sock.connect((SJL06_IP, SJL06_Port))
    sock.send(msg)
    resp = sock.recv(1024)
    print 'Reply from server:[' + resp[2:len(resp)] + ']\n'
    sock.close()

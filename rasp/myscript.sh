#raspivid -o - -t 0 -hf -w 640 -h 480 -fps 25 | cvlc -vvv stream:///dev/stdin --sout '#rtp{sdp=rtsp://:8554}' :demux=h264

cvlc v4l2:///dev/video0 --v4l2-width 640 --v4l2-height 480 --v4l2-chroma h264 --sout '#standard{access=http,mux=ts,dst=0.0.0.0:5000}'

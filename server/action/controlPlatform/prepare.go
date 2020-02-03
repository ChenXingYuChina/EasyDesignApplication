package controlPlatform

import (
	"net"
	"net/http"
)

// 请优先使用http包中给定的，仅做管理备用
// please use the default ones in the net/http package just prepare for manage server without stop.
type HttpServer struct {
	Address net.TCPAddr
	mux http.ServeMux
}

func (s *HttpServer) ListenAndServer() (err error) {
	l, err := net.ListenTCP("tcp", &s.Address)
	if err != nil {
		return err
	}
	return http.Serve(l, &s.mux)
}

func (s *HttpServer) Handle(pattern string, handler http.Handler) {
	s.mux.Handle(pattern, handler)
}

func (s *HttpServer) HandleFunc(pattern string, handler func(http.ResponseWriter, *http.Request))  {
	s.mux.HandleFunc(pattern, handler)
}

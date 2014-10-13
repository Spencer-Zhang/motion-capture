require 'net/http'
require 'cgi'

class DataController < ApplicationController
  def update
    analog = JSON.parse(request.body.read)["ANALOG"]
    temperature = (analog["B"] * 0.2222 - 61.111).to_i

    uri = URI('http://198.61.189.122:80')
    req = Net::HTTP::Put.new(uri)
    req.set_form_data({temperature: temperature})

    puts uri.port

    res = Net::HTTP.start(uri.hostname, uri.port, read_timeout:1) do |http|
      http.request(req)
    end

    render nothing: true
  end
end

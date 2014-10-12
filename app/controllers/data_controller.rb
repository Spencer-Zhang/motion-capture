class DataController < ApplicationController
  def update

    p request.body.read

    render nothing:true
  end
end

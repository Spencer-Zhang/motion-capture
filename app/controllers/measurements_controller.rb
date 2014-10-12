class MeasurementsController < ApplicationController
  def update
    Measurement.create(params['temperature'].to_i)
    render nothing:true
  end

  def get
    measurement = Measurement.order("created_at").last
    render json: measurement
  end
end

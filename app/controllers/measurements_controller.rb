class MeasurementsController < ApplicationController
  def update

    p params

    # Measurement.create(request.body.read)

    render nothing:true
  end
end
